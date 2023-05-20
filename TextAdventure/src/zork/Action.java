package zork;

public class Action {
    private String name;
    private String response;
    private boolean mercyOption;

    public Action(String name, String response, boolean mercyOption) {
        this.name = name;
        this.response = response;
        this.mercyOption = mercyOption;
    }

    public Action(String name, String response) {
        this.name = name;
        this.response = response;
    }

    public String getName() {
        return name;
    }

    public String getResponse() {
        return response;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setMercyOption(boolean mercyOption) {
        this.mercyOption = mercyOption;
    }

}
