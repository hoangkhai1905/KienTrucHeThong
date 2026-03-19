package iuh.fit.design_patterns.adapter;

public class XmlToJsonAdapter implements JsonWebService {
    private XmlSystem xmlSystem;

    public XmlToJsonAdapter(XmlSystem xmlSystem) {
        this.xmlSystem = xmlSystem;
    }

    @Override
    public void requestJson(String jsonData) {
        String xmlData = convertToXml(jsonData);
        System.out.println("Adapter converted JSON to XML: " + xmlData);
        xmlSystem.receiveXml(xmlData);
    }

    private String convertToXml(String jsonData) {
        // Simple mock conversion
        String content = jsonData.replaceAll("[{}\"]", "").replace(":", "=").trim();
        return "<request>" + content + "</request>";
    }
}


