package uk.ac.ebi.metabolomes.parser.brenda;

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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Citation other = (Citation) obj;
        if (this.pubmedId != other.pubmedId) {
            return false;
        }
        if(this.pubmedId == null) {
            if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + (this.pubmedId !=null ? this.pubmedId.hashCode() : 0);
        hash = 11 * hash + (this.title != null ? this.title.hashCode() : 0);
        return hash;
    }


}
