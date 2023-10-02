import BaseClass from "../util/baseClass";
import axios from 'axios'

/**
 * Client to call the MusicPlaylistService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
 */
export default class ExampleClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getExample', 'createExample', 'getAllCareers', 'getCareerById', 'updateCareerById', 'searchCareerById', 'createCareer', 'deleteCustomerById'];
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
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    /**
     * Gets the concert for the given ID.
     * @param id Unique identifier for a concert
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The concert
     */
    async getExample(id, errorCallback) {
        try {
            const response = await this.client.get(`/example/${id}`);
            return response.data;
        } catch (error) {
            this.handleError("getExample", error, errorCallback)
        }
    }

    async createExample(name, errorCallback) {
        try {
            const response = await this.client.post(`example`, {
                name: name
            });
            return response.data;
        } catch (error) {
            this.handleError("createExample", error, errorCallback);
        }
    }
    // Career controller --------------------------------------------------------------------------------------------------
    async getUserAccounts(user, errorCallback){
        try {
            const response = await this.client.get(`/Career/ ${userId}`)
        } catch (error){
            this.handleError("getUserAccounts", error, errorCallback);
        }
    }


    // Career controller --------------------------------------------------------------------------------------------------

    async getAllCareers(errorCallback){
        try {
            const response = await this.client.get(`Career`);
            return response.data;
    } catch (error){
        this.handleError("getAllCareers",error, errorCallback);
        }
    }

    async getCareerById(Id, errorCallback){
        try {
            const response = await this.client.get(`/Career/ ${Id}`);
            return response.data;
        } catch (error){
            this.handleError("getCareer" , error, errorCallback);
        }
    }


    async updateCareerById( Id,name, errorCallback){
        try {
        const response = await this.client.post(`/Career/ ${Id}`, {
            name: name
        });
        return response.data;
        } catch (error){
            this.handleError("updateCareerById", error, errorCallback);
        }
    }

    async searchCareerById(Id, errorCallback){
        try {
            const response = await this.client.get(`Career ${Id}`)
            return response.data;
        }catch (error){
            this.handleError("searchCareerById", error, errorCallback);
        }

    }

    async createCareer(name, errorCallback){
        try {
            const response = await this.client.post(`Career`, {
                name : name
            });
            return response.data;
        }catch (error){
            this.handleError("createCareer", error, errorCallback);
        }
    }

    async deleteCustomerById(Id, errorCallback){
try {
    const response = await this.client.delete(`/Career/ ${Id} `)
    return response.data;

} catch (error){
    this.handleError("deleteCustomerById", error, errorCallback);
}
    }

    async getUserAccount(userId, errorCallback){
        try {
            const response = await this.client.get(`/Career/user/ ${userId}`)

        } catch (error){

        }
    }


    // Company controller --------------------------------------------------------------------------------------------------



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
