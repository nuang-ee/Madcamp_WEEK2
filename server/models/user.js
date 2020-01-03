const mongoose = require ('mongoose');
const contacts = require('./contacts')
const Schema = mongoose.Schema;

/**
 * Create Schema
 */
const userSchema = new Schema({
    _id: {
        type: Number,
        required: "_id is essential",
        ref: "Contact"  // refers model "Contact"
    },
    facebook_id: {  // pending encryption
        type: String,
        required: "facebook_id is essential"
    },
    password: {     // pending encryption
        type: String,
        required: "password is essential"
    },
    contacts: {
        type: contactSchema
    }
});

module.exports = mongoose.model ('User', userSchema);