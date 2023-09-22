import BaseClass from "../util/baseClass";
import axios from 'axios'

export default class ExampleClient extends BaseClass {

    constructor(props = {}) {
        super();

        //dont forget to change these as needed
        const methodsToBind = ['clientLoaded', 'getExample', 'createExample'];
    }



}