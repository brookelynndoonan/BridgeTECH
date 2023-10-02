import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";

class SignUpPage extends BaseClass {


    constructor() {
        super();
        this.bindClassMethods(['onCreate'], this);
        this.dataStore = new DataStore();
    }

    async mount(){
        document.getElementById('signUp-form').addEventListener('submit', this.onCreate);
        this.client = new ExampleClient();

        // this.dataStore.addChangeListener(this.renderSignUp)
    }


    async onCreate(event){
        event.preventDefault();
        this.dataStore.set("user", null);

        let name = document.getElementsByName("firstName", "email", "psw").value;

        const  createdUser = await this.client.createCareer(name , this.errorHandler);
        this.dataStore.set("user", createdUser);

        if (createdUser){
            this.showMessage(`Created ${createdUser.name}!`)
        } else {
            this.errorHandler("Error creating!  Try again...")
        }
    }

    async onDelete(event){

    }
}

const main = async () => {
    const  signUpPage = new SignUpPage();
    await signUpPage.mount();
};

window.addEventListener('DOMContentLoaded', main);

//sign up page add new information and creates a new user page
//to be pushed to the lambdas through the career controller