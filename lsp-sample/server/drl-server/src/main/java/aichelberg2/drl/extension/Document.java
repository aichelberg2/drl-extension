package aichelberg2.drl.extension;

public class Document {
  private final String uri;
  private final String text;

  public Document(String uri, String text) {
    this.uri = uri;
    this.text = text;
  }

  public String getUri() {
    return uri;
  }

  public String getText() {
    return text;
  }
}
