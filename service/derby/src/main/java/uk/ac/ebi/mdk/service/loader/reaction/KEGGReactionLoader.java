package uk.ac.ebi.mdk.service.loader.reaction;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.service.connection.reaction.KEGGReactionConnection;
import uk.ac.ebi.mdk.service.loader.AbstractDerbyLoader;
import uk.ac.ebi.mdk.service.loader.location.SystemLocation;
import uk.ac.ebi.mdk.service.schema.ReactionSchema;
import uk.ac.ebi.mdk.service.exception.MissingLocationException;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * KEGGReactionLoader - 05.03.2012 <br/>
 * <p/>
 * Load's KEGG Reactions into a derby database
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGReactionLoader
        extends AbstractDerbyLoader {

    private static final Logger LOGGER = Logger.getLogger(KEGGReactionLoader.class);
    private Pattern ACCESSION = Pattern.compile("([CDGR]\\d+)");


    public KEGGReactionLoader() {

        super(new KEGGReactionConnection());

        addRequiredResource("KEGG Reaction",
                            "KEGG Reaction flatfile (kegg/ligand/reaction)",
                            ResourceFileLocation.class);

    }

    @Override
    public void update() throws MissingLocationException, IOException {

        ResourceFileLocation location = getLocation("KEGG Reaction");

        KEGGReactionParser parser = new KEGGReactionParser(location.open(), KEGGField.ENTRY, KEGGField.EQUATION, KEGGField.ENZYME);

        ReactionSchema schema = new ReactionSchema(getConnection());

        Map<KEGGField, StringBuilder> entry;
        while ((entry = parser.readNext()) != null) {

            if (isCancelled()) break;

            String equation = entry.get(KEGGField.EQUATION).toString();
            String ec       = entry.containsKey(KEGGField.ENZYME) ? entry.get(KEGGField.ENZYME).toString().trim() : "";
            String[] sides = equation.split("<=>");

            String[][] left  = getParticipants(sides[0]);
            String[][] right = getParticipants(sides[1]);

            Matcher matcher = ACCESSION.matcher(entry.get(KEGGField.ENTRY).toString());

            if(!ec.isEmpty())
                ec = ec.split("\\s+")[0].trim();

            if(matcher.find()){
                String accession = matcher.group(1);
                System.out.println(accession);
                try{
                    schema.insert(accession, ec, left, right);
                }catch (SQLException ex){
                    LOGGER.warn("Could not inser values",ex);
                }
            }





        }

        try{
            schema.dump();
            getConnection().close();
        } catch (Exception ex){
            LOGGER.warn(ex);
        }
        location.close();

    }

    public String[][] getParticipants(String side) {

        String[][] participants = new String[0][2];
        
        for (String participant : side.split("(?<=\\s|\\d)\\+(?=\\s|[CDG]|\\d+ [CDG])")) {
            Matcher matcher = ACCESSION.matcher(participant);
            if (matcher.find()) {
                
                String accession = matcher.group(1);
                String coef = normaliseCoefficent(matcher.replaceAll("").replaceAll("[()]", "").trim());
                
                participants = Arrays.copyOf(participants, participants.length + 1);
                participants[participants.length - 1] = new String[2];
                participants[participants.length - 1][0] = coef;
                participants[participants.length - 1][1] = accession;

            }
        }

        return participants;

    }

    private static Integer DEFAULT_N = 2;
    private static Integer DEFAULT_M = 2;


    Pattern times_modifier = Pattern.compile("\\A(\\d+)[n|m]");
    Pattern plus_minus = Pattern.compile("\\A[n|m]([+|-])(\\d+)");


    public String normaliseCoefficent(String coef) {

        if (coef.isEmpty())
            return "1";

        Matcher matcherTimes = times_modifier.matcher(coef);
        Matcher plusMinuseMatcher = plus_minus.matcher(coef);

        if (coef.contains("n")) {
            if (coef.contains("m")) {
                coef = Integer.toString(DEFAULT_N + DEFAULT_M);
            } else if (plusMinuseMatcher.find()) {
                String op = plusMinuseMatcher.group(1);
                String val = plusMinuseMatcher.group(2);

                if (op.equals("+")) {
                    coef = Integer.toString(DEFAULT_N + Integer.parseInt(val));
                } else if (op.equals("-")) {
                    coef = Integer.toString(DEFAULT_N - Integer.parseInt(val));
                }

            } else if (matcherTimes.find()) {
                coef = Integer.toString(Integer.parseInt(matcherTimes.group(1)) * DEFAULT_N);
            } else {
                coef = DEFAULT_N.toString();
            }
        } else if (coef.contains("m")) {
            if (plusMinuseMatcher.find()) {
                String op = plusMinuseMatcher.group(1);
                String val = plusMinuseMatcher.group(2);

                if (op.equals("+")) {
                    coef = Integer.toString(DEFAULT_M + Integer.parseInt(val));
                } else if (op.equals("-")) {
                    coef = Integer.toString(DEFAULT_M - Integer.parseInt(val));
                }
            } else {
                coef = DEFAULT_M.toString();
            }
        }
        return coef;
    }

    public static void main(String[] args) throws IOException, MissingLocationException {

        BasicConfigurator.configure();

        new KEGGReactionConnection().clean();

        KEGGReactionLoader loader = new KEGGReactionLoader();

        if (loader.canBackup()) loader.backup();

        loader.addLocation("KEGG Reaction", new SystemLocation("/databases/kegg/ligand/reaction"));

        loader.update();


    }


    // adapted chemet-io, didn't want to have a dependency as IO is quite messy with a lot of
    // old classes and redundant dependencies
    class KEGGReactionParser {
        private BufferedReader reader;
        private EnumSet<KEGGField> filter = EnumSet.noneOf(KEGGField.class);

        public KEGGReactionParser(InputStream stream, KEGGField... field) {
            // 12 mb of buffer
            this.reader = new BufferedReader(new InputStreamReader(stream), 1024 * 12);
            for (KEGGField f : field) {
                filter.add(f);
            }
        }

        public Map<KEGGField, StringBuilder> readNext() throws IOException {

            StringBuilder sb = new StringBuilder(1000);
            @SuppressWarnings("unchecked")
            Map<KEGGField, StringBuilder> map = new EnumMap(KEGGField.class);

            String line;
            KEGGField field = null;
            while ((line = reader.readLine()) != null && !line.equals("///")) {

                String key = line.substring(0, Math.min(line.length(), 12)).trim();
                field = key.isEmpty() ? field : KEGGField.valueOf(key);

                if (filter.contains(field)) {
                    if (!map.containsKey(field)) {
                        map.put(field, new StringBuilder(500));
                    }
                    map.get(field).append(line.substring(12));
                }

            }
            return line == null ? null : map;
        }

    }

    public enum KEGGField {
        ENTRY, NAME,
        DEFINITION, EQUATION,
        ENZYME, COMMENT,
        RPAIR, PATHWAY,
        ORTHOLOGY, REMARK,
        REFERENCE
    }

}
