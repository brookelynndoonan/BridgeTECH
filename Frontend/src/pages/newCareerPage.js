import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";

class NewCareerPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onCreate'], this);
        this.dataStore = new DataStore();
        this.client = new ExampleClient();
    }
    async mount(){
        document.getElementById('signUp-form').addEventListener('submit', this.onCreate);

    }
    async onCreate(event) {
         //create a job Post
        //     let careerId = this.dataStore.get('Id')
        event.preventDefault();
         this.dataStore.set("Career", null);
        let companyName = document.getElementById('company').value;
        let companyLocation = document.getElementById('job-location')
        let jobTitle = document.getElementById("job-title").value;
        let jobDescription = document.getElementById('job-description').value;
        let jobType = document.getElementById('job-type').value;

        const createdCareerPost = await this.client.createCareer(companyName,companyLocation, jobTitle, jobDescription, jobType);
        this.dataStore.set("Career", createdCareerPost);

        if(createdCareerPost){
            this.showMessage(`Created ${createdCareerPost.name}!`)
        }else {
            this.errorHandler("Error creating!  Try again...")
        }
     }
}
const main = async () => {
    const newCarerPage = new NewCareerPage();
    newCarerPage.mount();
};

window.addEventListener('DOMContentLoaded',main);