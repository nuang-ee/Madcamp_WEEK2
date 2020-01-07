const mongoose = require('mongoose');
const ContactSchema = require('./contact').schema;
const ImageSchema = require('./image').schema;
const claimerSchema = require('./claimer').schema;
const claimeeSchema = require('./claimee').schema;
const Schema = mongoose.Schema;

/**
 * Create Schema
 * https://stackoverflow.com/questions/43024285/embedding-schemas-is-giving-error/43024503
 */
const userSchema = new Schema({
    uid: { // pending encryption
        type: String,
        required: true,
        default: ""
    },
    name: {
        type: String,
        default: ""
    },
    contact: [{
        type: ContactSchema
    }],
    image: [{
        type: ImageSchema
    }],
    claimee: [{
        type: claimeeSchema
    }],
    claimer: [{
        type: claimerSchema
    }]
});

module.exports = mongoose.model('User', userSchema);