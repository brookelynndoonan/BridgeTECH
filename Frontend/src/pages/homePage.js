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
        document.getElementById('create-form').addEventListener('submit', this.onCreate);
        this.client = new ExampleClient();

        this.dataStore.addChangeListener(this.renderExample)
    }
}