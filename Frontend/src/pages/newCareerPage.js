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
        let companydescription = document.getElementById('company').value;
        let location = document.getElementById('job-location').value;
        let name = document.getElementById("job-title").value;
        let jobdescription = document.getElementById('job-description').value;
        // let jobType = document.getElementById('job-type').value;

        const createdCareerPost = await this.client.createCareer(name,location, companydescription, jobdescription);
        this.dataStore.set("Career", createdCareerPost);

        if(createdCareerPost){
            this.showMessage(`Created ${createdCareerPost.name}!`)
            window.location.href='careerPage.html'
        }else {
            this.errorHandler("Error creating!  Try again...")
        }
     }
}
const main = async () => {
    const newCareerPage = new NewCareerPage();
    newCareerPage.mount();
};

window.addEventListener('DOMContentLoaded',main);