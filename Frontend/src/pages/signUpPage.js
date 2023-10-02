import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";

class signUpPage extends BaseClass {


    constructor() {
        super();
        this.bindClassMethods(['onCreate'], this);
        this.dataStore = new DataStore();
    }

    async mount(){

    }

    async renderSignUp(){

    }

    async onCreate(event){
        event.preventDefault();
        this.dataStore.set("", null);

        let name = document.getElementsByName("firstName", "email", "psw").value;

        const  createdUser = await this.client.createCareer(name , this.errorHandler);
        this.dataStore.set("", createdUser);

        if (createdUser){
            this.showMessage(`Created ${createdUser.name}!`)
        } else {
            this.errorHandler("Error creating!  Try again...")
        }
    }

    async onDelete(event){

    }
}

//sign up page add new information and creates a new user page
//to be pushed to the lambdas through the career controller