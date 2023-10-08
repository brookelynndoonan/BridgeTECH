import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";

class CareerPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['renderCareer'], this);
        this.dataStore = new DataStore();
        this.client = new ExampleClient();
    }

    async mount() {
        // const urlParams = new URLSearchParams(window.location.search);
        // const careerId = urlParams.get('Id');
        // const career = await this.client.getAllCareers(careerId, this.errorHandler);
        // this.dataStore.set(career);
        // this.dataStore.set("career", careerId);
        await this.renderCareer();
    }

    // async onCreate(event) {
    //     //create a job Post
    //     event.preventDefault();
    //     this.dataStore.set("Career", null);
    //
    //     let careerId = this.dataStore.get('Id')
    //     let jobNameTitle = document.getElementById("job-name-title").value;
    //     let jobName = document.c
    //
    //
    // }

    async renderCareer() {
        let resultArea = document.getElementById('job-posting-container');
        const careers = await this.client.getAllCareers(); // Use getAllCareers without filters


        if (careers && careers.length > 0) {
            resultArea.innerHTML = careers
                .map(
                    (career) => `
                    <p class="job-postings-container">
                        <ul class="job-listings">
                            <li class="individual-job-post">
                                <div>Id: ${career.id}</div>
                                <div><h4>Name: ${career.name}</h4></div>
                                <div>Company: ${career.companydescription}</div>
                                <div>Job Description: ${career.jobdescription}</div>
                            </li>
                        </ul>
                    </p>
                `
                )
                .join("");
        } else {
            resultArea.innerHTML = "No Industry information available.";
        }
        // resultArea.appendChild(careers);
    }

    // onDelete(){
    //
    // }
}

const main = async () => {
    const careerPage = new CareerPage();
    await careerPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
