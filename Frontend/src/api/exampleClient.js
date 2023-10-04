import BaseClass from "../util/baseClass";
import axios from 'axios'


export default class ExampleClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getAllCareers', 'getCareerById', 'updateCareerById', 'searchCareerById', 'createCareer', 'deleteCustomerById', 'getUserAccount' , 'createUserAccount' , 'addNewCompany', 'updateCompany', 'getAllCompanies', 'getCompaniesByName', 'searchCompaniesById', 'searchCompanyByName', 'getAllCompaniesByName', 'deleteCompanyById', 'addNewIndustry', 'updateIndustry', 'getAllIndustries' ,'getAllIndustriesByName', 'searchIndustryByName', 'searchIndustryById', 'deleteIndustryById'];
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

    // Career controller --------------------------------------------------------------------------------------------------
    // async getUserAccounts(user, errorCallback){
    //     try {
    //         const response = await this.client.get(`/Career/ ${userId}`)
    //     } catch (error){
    //         this.handleError("getUserAccounts", error, errorCallback);
    //     }
    // }


    // Career controller --------------------------------------------------------------------------------------------------

    async getAllCareers(errorCallback){
        try {
            const response = await this.client.get(`/Career/`);
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

    async updateCareerById( Id, name, errorCallback){
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
            const response = await this.client.get(`/Career/ ${Id}`)
            return response.data;
        }catch (error){
            this.handleError("searchCareerById", error, errorCallback);
        }

    }

    async createCareer(name, errorCallback){
        try {
            const response = await this.client.post(`/Career/`, {
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
            return response.data;
        } catch (error){
            this.handleError("getUserAccount", error, errorCallback);
        }
    }

    async createUserAccount(){

    }


    // Company controller --------------------------------------------------------------------------------------------------
    async addNewCompany(name , errorCallback){
        try{
            const response = await this.client.post(`/Company/`, {
                name : name
            });
            return response.data;
        } catch (error){
            this.handleError("addNewCompany", error, errorCallback);
        }
    }

    async updateCompany(Id, name, errorCallback){
        try {
            const response = await this.client.post(`/Company/ ${Id}`, {
                name : name
            });
            return response.data;
        } catch (error){
            this.handleError("updateCompany", error, errorCallback);
        }
    }

    async getAllCompanies(errorCallback) {
        try {
            const response = await this.client.get(`/Company/`)
            return response.data;
        } catch (error){
            this.handleError("getAllCompanies", error, errorCallback);
        }
    }

    async getAllCompaniesByName(Id, name, errorCallback){
        try {
        const response = await this.client.get(`/Company/ ${Id}`, {
            name :name
        });
        return response.data;
        } catch (error){
            this.handleError("getAllCompaniesByName", error, errorCallback);
        }
    }

    async searchCompaniesById(Id, errorCallback){
        try {
             const response = await this.client.get(`/Company/ ${Id}`)
              return response.data;
        } catch (error){
            this.handleError("searchCompaniesById", error, errorCallback);
        }
    }

    async searchCompanyByName(Name, errorCallback){
        try {
            const response = await this.client.get(`/Company/ ${Name}`)
            return response.data;
        } catch (error){
            this.handleError("searchCompanyByName", error, errorCallback);
        }
    }

    async deleteCompanyById(Id, errorCallback){
        try {
           const response = await this.client.delete(`/Company/ ${Id} `)
            return response.data;
        }catch (error){
            this.handleError("deleteCompanyById", error, errorCallback);
        }
    }

    // Industry controller --------------------------------------------------------------------------------------------------

    async addNewIndustry(errorCallback){
        try {
            const response = await this.client.post(`/Industry/`)
            return response.data;
        }catch (error){
            this.handleError("addNewIndustry", error, errorCallback);
        }
    }

    async updateIndustry(Id, errorCallback){
        try {
            const response = await this.client.post(`/Industry/ ${Id}`)
            return response.data;

        } catch (error){
            this.handleError("updateIndustry", error, errorCallback);
        }
    }

    async getAllIndustries(errorCallback){
        try {
            const response = await this.client.get(`/Industry/`)
            return response.data;
        }catch (error){
            this.handleError("getAllIndustries", error, errorCallback);
        }
    }

    async getAllIndustriesByName(byName, errorCallback){
        try {
            const response = await this.client.get(`/Industry/byName/`);
            return response.data;

        }catch (error){
            this.handleError("getAllIndustriesByName", error, errorCallback);
        }
    }

    async searchIndustryById(Id, errorCallback){
        try {
            const response = await this.client.get(`/Industry/industry/ ${Id}`)
            return response.data;
        }catch (error){
            this.handleError("searchIndustryById", error, errorCallback);
        }
    }

    async searchIndustryByName(IndustryName, industryName, errorCallBack){
       try {
           const response = await this.client.get(`/Industry/industryName/byIndustryName/ ${IndustryName}`,{
               name : industryName
           });
           return response.data;
       } catch (error){
           this.handleError("searchIndustryByName", error, errorCallBack);
       }

    }

    async deleteIndustryById(Id, errorCallBack){
        try {
            const  response = await this.client.delete(`/Industry/ ${Id}`)
            return response.data;

        }catch (error){
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
