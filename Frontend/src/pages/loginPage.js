import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";

class loginPage extends BaseClass {


    constructor() {
        super();
        this.bindClassMethods(['onGet', 'renderUser'], this);
        this.dataStore = new DataStore();
    }

    // async mount() {
    //     document.
    // }
}
// enters login information and gets user information
// from career controller connected to the lambda
// to bring user to the dashboard page
//onGetCustomerById