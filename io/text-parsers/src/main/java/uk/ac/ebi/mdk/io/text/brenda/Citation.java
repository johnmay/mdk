/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.io.text.brenda;

public class Citation implements Comparable<Citation>{
	private Integer pubmedId;
	private String desc;
	private String title;
	
	public Citation() {
	}

	/**
	 * @return the pubmedId
	 */
	public Integer getPubmedId() {
		return pubmedId;
	}

	/**
	 * @param pubmedId the pubmedId to set
	 */
	public void setPubmedId(int pubmedId) {
		this.pubmedId = pubmedId;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	//@Override
	public int compareTo(Citation o) {
		// TODO Auto-generated method stub
		if(this.pubmedId != null && o.pubmedId != null)
			return Integer.valueOf(this.pubmedId).compareTo(Integer.valueOf(o.pubmedId));
		else if(this.title != null && o.desc != null)
			return this.title.compareToIgnoreCase(o.title);
		else if(this.desc!=null && o.desc!=null)
                    return this.desc.compareToIgnoreCase(o.desc);
                else
                    return 0;

	}
	public boolean hasPubMedID() {
		if(this.pubmedId > 0)
			return true;
		else return false;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Citation citation = (Citation) o;

        if (pubmedId != null ? !pubmedId.equals(citation.pubmedId) : citation.pubmedId != null) return false;
        if (pubmedId == null && title != null ? !title.equals(citation.title) : citation.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pubmedId != null ? pubmedId.hashCode() : 0;
        result = 31 * result + (pubmedId == null && title != null ? title.hashCode() : 0);
        return result;
    }
}
