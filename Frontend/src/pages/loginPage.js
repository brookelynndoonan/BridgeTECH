import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";

class LoginPage extends BaseClass {


    constructor() {
        super();
        this.bindClassMethods(['onGet'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        document.getElementById('login-form').addEventListener('submit', this.onGet);
        this.client = new ExampleClient();
    }

    async onGet(event) {
        event.preventDefault();

        let email = document.getElementById("email-field").value;
        let password = document.getElementById("password-field").value;
        this.dataStore.set("user", null);

        let result = await this.client.getUserAccount(email, password, this.errorHandler);
        this.dataStore.set("user", result);

        if (result){
            this.showMessage(`Got ${result.name}!`)
            window.location.href='dashBoard.html'
        } else {
            this.errorHandler("Error doing GET! Try again...");
        }

    }
}

const main = async () => {
    const loginPage = new LoginPage();
   await loginPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
// enters login information and gets user information
// from career controller connected to the lambda
// to bring user to the dashboard page
//onGetCustomerById