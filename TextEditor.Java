// Document Editor

// Features:
// 1-> only support text and image as of now( but should be scalable )
// 
// DocumentElement( interface ) -> render()
// child classes will inherit this
// textElement imageElement and then it's easily expandable
// (open close principle)
// override render according to inherited classes
// Create Class Document->( List of DocumentElement crud operations)
// Document has a (1 is to Many 1---*) DocumentElement
// Document ->( vector<DocumentElement>, addElement(), removeELement())
// PersistenceClass ( save() )(interface) ->(SaveToFile, saveToDrive()---)
// Class DocumentEditor has a Document, has a PersistenceClass
// addText(), addImage(), renderDoc()
// code will help me understand better
// Following SRP,OCP, LSP, ISP,DIP


interface DocumentElement{
    public abstract String render();
}

Class TextElement implements DocumentElement{
    private String text;
    public String TextElement( String text ){
        this.text = text;
    }
    @Override
    public String render(){
        return text;
    }
}

class ImageElement implements DocumentElement{
    private String imageUrl;
    public String ImageElement( String imageUrl ){
        this.imageUrl = imageUrl;
    }
    @Override
    public String render(){
        return imageUrl;
    }
}

Class Document{
    // DIP -> directly talking to intf but not to low level  modules
    private List<DocumentElement> documentElement = new ArrayList<>();
    public void addElement(){

    }
    public void removeELement(){

    }
    public String render(){

    }
}

// Class to save

interface Persistence{
    public void save();
}

class SaveToDb implements Persistence{
    @Override
    public void save(){

    }
}

class DocumentEditor{
    private Document document;
    private Persistence persistence;
    public void DocumentEditor(){
        this.document=document;
        this,persistence=persistence;
    }
     public void addText(String text) {
        document.addElement(new TextElement(text));
    }

    public void addImage(String imagePath) {
        document.addElement(new ImageElement(imagePath));
    }

    public String renderDocument() {
        // will iterate through all the items and call the render
        document.render();
    }

}
