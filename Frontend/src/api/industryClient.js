import BaseClass from "../util/baseClass";
import axios from 'axios'


export default class IndustryClient extends BaseClass {

    constructor(props =  {}) {
        super();
        const methodsToBind = ['clientLoaded', 'addNewIndustry', 'updateIndustry', 'getAllIndustries', 'getAllIndustriesByName', 'searchIndustryByName', 'searchIndustryById', 'deleteIndustryById'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    // Industry controller --------------------------------------------------------------------------------------------------

    async addNewIndustry(errorCallback) {
        try {
            const response = await this.client.post(`/Industry/`)
            return response.data;
        } catch (error) {
            this.handleError("addNewIndustry", error, errorCallback);
        }
    }

    async updateIndustry(Id, errorCallback) {
        try {
            const response = await this.client.post(`/Industry/ ${Id}`)
            return response.data;

        } catch (error) {
            this.handleError("updateIndustry", error, errorCallback);
        }
    }

    async getAllIndustries(errorCallback) {
        try {
            const response = await this.client.get(`/Industry/`)
            return response.data;
        } catch (error) {
            this.handleError("getAllIndustries", error, errorCallback);
        }
    }

    async getAllIndustriesByName(byName, errorCallback) {
        try {
            const response = await this.client.get(`/Industry/byName/`);
            return response.data;

        } catch (error) {
            this.handleError("getAllIndustriesByName", error, errorCallback);
        }
    }

    async searchIndustryById(Id, errorCallback) {
        try {
            const response = await this.client.get(`/Industry/industry/ ${Id}`)
            return response.data;
        } catch (error) {
            this.handleError("searchIndustryById", error, errorCallback);
        }
    }

    async searchIndustryByName(IndustryName, industryName, errorCallBack) {
        try {
            const response = await this.client.get(`/Industry/industryName/byIndustryName/ ${IndustryName}`, {
                name: industryName
            });
            return response.data;
        } catch (error) {
            this.handleError("searchIndustryByName", error, errorCallBack);
        }

    }

    async deleteIndustryById(Id, errorCallBack) {
        try {
            const response = await this.client.delete(`/Industry/ ${Id}`)
            return response.data;

        } catch (error) {
            this.handleError("deleteIndustryById", error, errorCallBack);
        }
    }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */

    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}