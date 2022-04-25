package com.iserhand.inviochallengefinal;

public class Cat {
    private String id;
    private String name;
    private String image;
    private String description;
    private String wiki_url;
    private String origin;
    private String life_span;
    private int energy_level;
    public String getLife_span() {
        return life_span;
    }
    public void setLife_span(String life_span) {
        this.life_span = life_span;
    }

    public int getEnergy_level() {
        return energy_level;
    }

    public void setEnergy_level(int energy_level) {
        this.energy_level = energy_level;
    }

    public int getShedding_level() {
        return shedding_level;
    }

    public void setShedding_level(int shedding_level) {
        this.shedding_level = shedding_level;
    }

    public int getSocial_needs() {
        return social_needs;
    }

    public void setSocial_needs(int social_needs) {
        this.social_needs = social_needs;
    }

    public int getChild_friendly() {
        return child_friendly;
    }

    public void setChild_friendly(int child_friendly) {
        this.child_friendly = child_friendly;
    }

    public int getDog_friendly() {
        return dog_friendly;
    }

    public void setDog_friendly(int dog_friendly) {
        this.dog_friendly = dog_friendly;
    }

    public int getVocalisation() {
        return vocalisation;
    }
    public void setVocalisation(int vocalisation) {
        this.vocalisation = vocalisation;
    }
    public int getHealth_issues() {
        return health_issues;
    }
    public void setHealth_issues(int health_issues) {
        this.health_issues = health_issues;
    }
    private int shedding_level;
    private int social_needs;
    private int child_friendly;
    private int dog_friendly;
    private int vocalisation;
    private int health_issues;
    private int isFav;
    public int getIsFav() {
        return isFav;
    }

    public void setIsFav(int isFav) {
        this.isFav = isFav;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWiki_url() {
        return wiki_url;
    }

    public void setWiki_url(String wiki_url) {
        this.wiki_url = wiki_url;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
