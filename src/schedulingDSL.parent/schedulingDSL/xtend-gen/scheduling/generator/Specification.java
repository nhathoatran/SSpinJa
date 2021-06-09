package scheduling.generator;

@SuppressWarnings("all")
public class Specification {
  private final String string = "004-034556";
  
  private final String[] parts = this.string.split("-");
  
  public void print() {
    String _get = this.parts[0];
    System.out.println(_get);
  }
}
