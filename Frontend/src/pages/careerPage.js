import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";

class CareerPage extends BaseClass{

    constructor() {
        super();
        this.bindClassMethods(['onGet','onCreate','renderCareer'], this);
        this.dataStore= new DataStore();
    }

    async mount(){
        const urlParams = new URLSearchParams(window.location.search);
        const careerId = urlParams.get('Id');
        const career = await this.client.getCareerById(careerId, this.errorHandler);
        this.dataStore.set(career);
        this.dataStore.set("career", careerId);
        await this.renderCareer();
    }

    async onCreate(event){
        //create a job Post
        event.preventDefault();
        this.dataStore.set("Career", null);

        let careerId = this.dataStore.get('Id')
        let jobNameTitle = document.getElementById("job-name-title").value;
        let jobName = document.c


    }

    async renderCareer(){
        let resultArea = document.getElementById('career-lists');
        const career = this.dataStore.getCareerById();

        //id,name, company description, job description
        console.log(career);
            resultArea.innerHTML = `
                <div id="career-lists">
                <div >Id: ${career.id}</div>
                <div >Name: ${name}</div>
                
                </div>
                `


        // if(career){
        //     resultArea.innerHTML = `
        //     <div>ID: ${career.id}</div>
        //     <div>Name: ${career.name}</div>
        // `
        // } else {
        //     resultArea.innerHTML = "No Item";
        // }
    }

    //
    // async onGet(){
    //
    // }


    // onDelete(){
    //
    // }
}

const main = async () => {
    const careerPage = new CareerPage();
   await careerPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
