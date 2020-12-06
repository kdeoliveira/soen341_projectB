package assembler;

import assembler.tokenization.EBNF;
import util.BinaryAddress;

public class LineStatement{

	private Node label;
	private Instruction instruction;
	private Comment comment;
	private int lineNumber;
	private BinaryAddress machineCode;
	private String typeEBNF;
	private boolean onlyLabels = false;
	private static final int MAXBITS = 32;

	/**
	 * Generates a line of statement based on Nodes and Comment
	 * 
	 * @param lineNumber
	 * @param label
	 * @param instruction
	 * @param comment
	 * @param type
	 */
	public LineStatement (int lineNumber, Node label, Node instruction, Comment comment, String type)
	{
		this.lineNumber = lineNumber;
		this.label = label;
		this.instruction = (Instruction) instruction;
		this.comment = comment;
		this.typeEBNF = type;
		this.checkBinaryValue(lineNumber);
	}
	public LineStatement ()
	{
		this.label = null;
		this.instruction = null;
		this.comment = new Comment();
	}
	public BinaryAddress getMachineCode() {
		return machineCode;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public String getTypeEBNF() {
		return typeEBNF;
	}
	public Node getLabel() {
		return label;
	}
	public boolean hasOnlyLabels(){
		return onlyLabels;
	}
	public void setLabel(Node label) {
		this.label = label;
		// this.label.setValue(new BinaryAddress((long) this.lineNumber, 16));
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public void setInstruction(Instruction instruction, boolean onlyLabels) {
		this.instruction = instruction;
		this.onlyLabels = onlyLabels;
	}
	public void setMachineCode(BinaryAddress machineCode) {
		this.machineCode = machineCode;
	}
	public void setTypeEBNF(String typeEBNF) {
		this.typeEBNF = typeEBNF;
	}

	public Node getOperand(){
		if(this.instruction == null)	return null;
		return this.instruction.getOperand();
	}

	public void setOperand(Node operand){
		if(this.instruction == null)	return;
		this.instruction.setOperand(operand);
	}

	public boolean hasInstruction(){
		return this.instruction != null;
	}

	public void checkBinaryValue(){
		if(this.instruction == null && this.label != null)
			this.machineCode = this.label.getValue();
		else if(this.instruction == null)
			this.machineCode = new BinaryAddress(0x0, 8);
		else
			this.machineCode = this.instruction.getValue();
	}

	public void checkBinaryValue(int lineNumber){
		this.lineNumber = lineNumber;
		if(label != null)
			label.setValue(new BinaryAddress((long) (this.lineNumber-1) * 2, 16));
		if(this.instruction == null && this.label != null)
			this.machineCode = this.label.getValue();
		else if(this.instruction == null)
			this.machineCode = new BinaryAddress(0x0, 8);
		else
			this.machineCode = this.instruction.getValue();
	}

	public Instruction getInstruction() {
		return instruction;
	}
	public String getComment() {
		return comment.toString();
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		if(machineCode.getFormat() > MAXBITS){
			str.append(this.machineCode.getHexCode().substring(0, 8)+"...");
		}else
			str.append(this.machineCode.getHexCode());
		
		if(this.label != null && typeEBNF.equals(EBNF.INHERENT2.name()))
			str.append(String.format("\t\t%s\t%s",this.label.getKey(), this.instruction));
		else if(this.label != null && this.instruction != null){
			if(this.machineCode.getFormat() > 32)
				str.append(String.format("\t%s\t%s ",this.label.getKey(), this.instruction));
			else
				str.append(String.format("\t\t%s\t%s ",this.label.getKey(), this.instruction));
			

		}
		else if(this.instruction == null && this.label != null)
			str.append(String.format("\t\t%s",label.getKey()));
		else
			if(this.machineCode.getFormat() > 24)
				str.append(String.format("\t\t%s", this.instruction == null ? "" : this.instruction));
			else
				str.append(String.format("\t\t\t%s", this.instruction == null ? "" : this.instruction));
		
		return str.append(this.comment == null ? "" : "\t"+this.comment).toString();
	}	
}