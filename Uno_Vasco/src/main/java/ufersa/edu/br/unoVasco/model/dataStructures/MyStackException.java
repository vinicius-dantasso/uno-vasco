package ufersa.edu.br.unoVasco.model.dataStructures;

public class MyStackException extends RuntimeException{
    private static final long serialVersionUID = 4652958143051993454L;

	private String description = "";
	
	public MyStackException(String description) {
            super(description);
            this.description = description;
        }
	
	public String getDescription() {
            return description;
	}
}
