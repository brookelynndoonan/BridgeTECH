import BaseClass from "../util/baseClass";
import axios from 'axios'


export default class ExampleClient extends BaseClass {

    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded', 'getAllCareers', 'getCareerById', 'updateCareerById', 'searchCareerById', 'createCareer', 'deleteCustomerById', 'getUserAccount', 'createUserAccount'];
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

    /**
     * Gets the concert for the given ID.
     * @param id Unique identifier for a concert
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The concert
     */


    // Career controller --------------------------------------------------------------------------------------------------

    async getAllCareers( errorCallback) {
        try {
            const response = await this.client.get(`/Career/`);
            return response.data;
        } catch (error) {
            this.handleError("getAllCareers", error, errorCallback);
        }
    }

    async getCareerById(Id, errorCallback) {
        try {
            const response = await this.client.get(`/Career/ ${Id}`);
            return response.data;
        } catch (error) {
            this.handleError("getCareer", error, errorCallback);
        }
    }

    async updateCareerById(Id, name, errorCallback) {
        try {
            const response = await this.client.post(`/Career/ ${Id}`, {
                name: name
            });
            return response.data;
        } catch (error) {
            this.handleError("updateCareerById", error, errorCallback);
        }
    }

    async searchCareerById(Id, errorCallback) {
        try {
            const response = await this.client.get(`/Career/ ${Id}`)
            return response.data;
        } catch (error) {
            this.handleError("searchCareerById", error, errorCallback);
        }

    }

    async createCareer(name, errorCallback) {
        try {
            const response = await this.client.post(`/Career/`, {
                name: name
            });
            return response.data;
        } catch (error) {
            this.handleError("createCareer", error, errorCallback);
        }
    }

    async deleteCustomerById(Id, errorCallback) {
        try {
            const response = await this.client.delete(`/Career/ ${Id} `)
            return response.data;

        } catch (error) {
            this.handleError("deleteCustomerById", error, errorCallback);
        }
    }

    async getUserAccount(userId, errorCallback) {
        try {
            const response = await this.client.get(`/Career/user/ ${userId}`)
            return response.data;
        } catch (error) {
            this.handleError("getUserAccount", error, errorCallback);
        }
    }

    async createUserAccount(name, lastName, email, password, accountType, errorCallback) {
        try {
            const response = await this.client.post(`/Career/user`, {
                "name": name,
                "lastName": lastName,
                "email": email,
                "password": password,
                "accountType": accountType
            });
            return response.data;
        } catch (error) {
            this.handleError("createUserAccount", error, errorCallback);
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
