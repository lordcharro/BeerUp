package pt.charroapps.beerup.model;

/**
 * Created by ivolopes on 05/02/16.
 */


import java.util.ArrayList;
import java.util.List;

public class Beer {

    private Integer currentPage;
    private Integer numberOfPages;
    private Integer totalResults;
    private List<Datum> data = new ArrayList<Datum>();
    private String status;

    /**
     * @return The currentPage
     */
    public Integer getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage The currentPage
     */
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * @return The numberOfPages
     */
    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    /**
     * @param numberOfPages The numberOfPages
     */
    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    /**
     * @return The totalResults
     */
    public Integer getTotalResults() {
        return totalResults;
    }

    /**
     * @param totalResults The totalResults
     */
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    /**
     * @return The data
     */
    public List<Datum> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<Datum> data) {
        this.data = data;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
