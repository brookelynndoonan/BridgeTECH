import BaseClass from "../util/baseClass";
import axios from 'axios'


export default class CompanyClient extends BaseClass {
    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded','addNewCompany', 'updateCompany', 'getAllCompanies', 'getAllCompaniesByName', 'searchCompaniesById', 'searchCompanyByName', 'getAllCompaniesByName', 'deleteCompanyById'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady();
        }
    }

    // Company controller --------------------------------------------------------------------------------------------------
    async addNewCompany(name, errorCallback) {
        try {
            const response = await this.client.post(`/Company/`, {
                name: name
            });
            return response.data;
        } catch (error) {
            this.handleError("addNewCompany", error, errorCallback);
        }
    }

    async updateCompany(Id, name, errorCallback) {
        try {
            const response = await this.client.post(`/Company/ ${Id}`, {
                name: name
            });
            return response.data;
        } catch (error) {
            this.handleError("updateCompany", error, errorCallback);
        }
    }

    async getAllCompanies(errorCallback) {
        try {
            const response = await this.client.get(`/Company/`)
            return response.data;
        } catch (error) {
            this.handleError("getAllCompanies", error, errorCallback);
        }
    }

    async getAllCompaniesByName(Id, name, errorCallback) {
        try {
            const response = await this.client.get(`/Company/ ${Id}`, {
                name: name
            });
            return response.data;
        } catch (error) {
            this.handleError("getAllCompaniesByName", error, errorCallback);
        }
    }

    async searchCompaniesById(Id, errorCallback) {
        try {
            const response = await this.client.get(`/Company/ ${Id}`)
            return response.data;
        } catch (error) {
            this.handleError("searchCompaniesById", error, errorCallback);
        }
    }

    async searchCompanyByName(Name, errorCallback) {
        try {
            const response = await this.client.get(`/Company/ ${Name}`)
            return response.data;
        } catch (error) {
            this.handleError("searchCompanyByName", error, errorCallback);
        }
    }

    async deleteCompanyById(Id, errorCallback) {
        try {
            const response = await this.client.delete(`/Company/ ${Id} `)
            return response.data;
        } catch (error) {
            this.handleError("deleteCompanyById", error, errorCallback);
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