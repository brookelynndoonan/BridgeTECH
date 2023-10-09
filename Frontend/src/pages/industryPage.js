import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import IndustryClient from "../api/industryClient";


class IndustryPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['renderIndustry'], this);
        this.dataStore = new DataStore();
        this.client = new IndustryClient();
    }

    async mount() {
        await this.renderIndustry();

    }

    async renderIndustry() {
        let resultArea = document.getElementById("industry-form-container");
        const industries = await this.client.getAllIndustries();

        if(industries && industries.length > 0) {
            resultArea.innerHTML = industries
                .map(
                    (industry) => `
                    <p class= "industry-form-container>
                        <ul class="industry-listings">
                            <li class="individual-industry-posts">
                                <div><h4>Name: ${industry.industryName}</h4></div>
                                <div>Description: ${industry.industryDescription}</div>
                                <div>Id: ${industry.industryId}</div>
                            </li>
                        </ul>
                    </p>
                `
                )
                        .join("");
        } else {
            resultArea.innerHTML = "No careers at this moment. Check back Later!"
        }
    }
}

const main = async () => {
    const industryPage = new IndustryPage();
    await industryPage.mount();
};

window.addEventListener('DOMContentLoaded', main);