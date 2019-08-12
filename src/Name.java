public class Name {

    public char[] name;

    public Name(int length) {
        name = new char[length];

    }

    public Name(String string) {
        name = new char[string.length()];

        for (int i = 0; i < string.length(); i++) {
            name[i] = string.charAt(i);
        }

    }

    // used to trim a String in quotation marks, used for JSON
    public Name(String s, boolean trim) {
        this(s.substring(1, (s.length() - 1)));
    }

    @Override
    public String toString() {
        String string = "";

        for (int i = 0; i < name.length; i++) {
            string += name[i];
        }

        return string;
    }

    public char getChar(int charAt) {
        return name[charAt];
    }


    // If this Name is greater then the input Name then return true
    public boolean compare(Name otherName) {

        for (int i = 0; i < Math.min(name.length, otherName.getLength()); i++) {


            if (name[i] > otherName.getChar(i)) {
                return true;
            } else if (name[i] < otherName.getChar(i)) {
                return false;
            }

        }


        return false;
    }




    public int getLength() {
        return name.length;
    }

    // Useful for bubble sort
    public void switchNames(Name otherName) {
        char[] tempName = name.clone();

        name = otherName.getName().clone();
        otherName.setName(tempName.clone());
    }

    public char[] getName() {
        return name;
    }

    public void setName(char[] nameInput) {
        name = nameInput;
    }


}
