import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";

class SignUpPage extends BaseClass {


    constructor() {
        super();
        this.bindClassMethods(['onCreate'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        document.getElementById('signUp-form').addEventListener('submit', this.onCreate);
        this.client = new ExampleClient();
    }

    async onCreate(event) {
        event.preventDefault();
        this.dataStore.set("user", null);

        let name = document.getElementById("name").value;
        let lastName = document.getElementById("lastname").value;
        let email = document.getElementById("email").value;
        let password = document.getElementById("password").value;
        let accountType = document.getElementById("accountType").value;

        console.log("method check!");

        const createdUser = await this.client.createUserAccount(name, lastName, email, password, accountType, this.errorHandler);
        this.dataStore.set("user", createdUser);

        if (createdUser) {
            this.showMessage(`Created ${createdUser.name}!`)
            window.location.href='dashBoard.html'
        } else {
            this.errorHandler("Error creating!  Try again...")
        }
    }
}
const main = async () => {
    const signUpPage = new SignUpPage();
    await signUpPage.mount();
};

window.addEventListener('DOMContentLoaded', main);

//sign up page add new information and creates a new user page
//to be pushed to the lambdas through the career controller