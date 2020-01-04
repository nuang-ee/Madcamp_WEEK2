const mongoose = require ('mongoose');
const ContactSchema = require('./contact').schema;
const ImageSchema = require('./image').schema;
const Schema = mongoose.Schema;

/**
 * Create Schema
 * https://stackoverflow.com/questions/43024285/embedding-schemas-is-giving-error/43024503
 */
const userSchema = new Schema({
    facebook_id: {  // pending encryption
        type: String,
        required: true
    },
    contact: {
        type: ContactSchema
    },
    images: {
        type: ImageSchema
    }
});

module.exports = mongoose.model ('User', userSchema);