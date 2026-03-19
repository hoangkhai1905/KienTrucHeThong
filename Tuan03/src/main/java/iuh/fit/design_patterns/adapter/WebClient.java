package iuh.fit.design_patterns.adapter;

public class WebClient {
    private JsonWebService jsonWebService;

    public WebClient(JsonWebService jsonWebService) {
        this.jsonWebService = jsonWebService;
    }

    public void sendRequest(String jsonData) {
        System.out.println("WebClient sending JSON: " + jsonData);
        jsonWebService.requestJson(jsonData);
    }
}

