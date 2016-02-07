package pt.charroapps.beerup.model;

/**
 * Created by ivolopes on 05/02/16.
 */

import io.realm.RealmObject;

public class Category extends RealmObject {

    private Integer id;
    private String name;
    private String createDate;

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The createDate
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate The createDate
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

}
