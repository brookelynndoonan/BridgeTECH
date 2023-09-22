import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";

class HomePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGet', 'renderExample'], this);
        this.dataStore = new DataStore();
    }
    async mount() {
        document.getElementById('get-title-content').addEventListener('submit', this.onGet);
      //  document.getElementById('create-form').addEventListener('submit', this.onCreate);
        this.client = new bridgeTechClient();

        this.dataStore.addChangeListener(this.renderExample)
    }


    // get login information // //or get images??
    //QUESTIONS TO ASK ELISE - the on get is similar to the

    async onGet(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let id = document.getElementById("login-signup").value;
        this.dataStore.set("example", null);

        let result = await this.client.getExample(id, this.errorHandler);
        this.dataStore.set("example", result);
        if (result) {
            this.showMessage(`Got ${result.name}!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }
}