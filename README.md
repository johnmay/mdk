# Model Development Kit (MDK)

The MDK is a modular [Java](http://www.java.com) open source library for simplifying procedures when handling metabolic models
and metabolomes. The kit was written at [EMBL-EBI](http://www.ebi.ac.uk) by [John May](http://www.github.com/johnmay)
and [Pablo Moreno](http://www.github.com/pcm32). The library was previously named CheMet after the
[Cheminformatics and Metabolism group](http://www.ebi.ac.uk/steinbeck) but was renamed as it's function
became more defined. The name MDK is derived from The [Chemistry Development Kit](http://cdk.sourceforge.com) (CDK)
which is one of the major constituent and used throughout the library to handle chemical structure.

### Key Features
*    intuitive object model for handling metabolic network/model chemistry (CDK) and gene/proteins (BioJava)
*    service framework using WebServices and NoSQL local storage
*    semantic annotation of cross-references
*    fast read/write of models
*    import/resolution of multiple formats including SBML, KGML, Excel
*    incubation/testing for tools prior to deployment in CDK
*    core library used by [Metingear: The Metabolic Development Environment](http://www.ebi.ac.uk/steinbeck-srv/metingear)
     ([GitHub](http://www.github.com/johnmay/metingear) project)

### Extra Dependencies

To build this project you need to install CAF (https://www.github.com/jwmay/caf) and MacWidgets. You need to install MacWidgets manually as follows: 

```
$ curl https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/macwidgets/mac_widgets-0.10.0.zip -O mac_widgets-0.10.0.zip
$ unzip mac_widgets-0.10.0.zip
$ mvn install:install-file -Dfile=MacWidgets.jar -Dpackaging=jar -DartifactId=macwidgets -DgroupId=com.explodingpixels -Dversion=0.10.0 -DgeneratePom=true
```

### License

The MDK is licensed under [Lesser General Public License 3.0](http://www.gnu.org/licenses/lgpl.html).

### Getting the library

#### Maven

The easiest way to use the library is with the [Apache Maven](http://maven.apache.org/) build automation tool and adding
a module dependency in the `pom.xml`. The example below shows adding the lucene service module, any required
dependencies that the defined module needs will automatically be downloaded by maven.

```
<dependency>
    <groupId>uk.ac.ebi.mdk</groupId>
    <artifactId>mdk-service-lucene</artifactId>
    <version>1.4.0</version>
</dependency>
```

The MDK modules are currently deployed to the EBI Maven Repository and the following should be defined in the
`<repositories>` section:

```
<repository>
    <id>ebi-repo</id>
    <name>EBI maven repository</name>
    <url>http://www.ebi.ac.uk/~maven/m2repo</url>
</repository>
```

#### Jar

Coming soon





