const mongoose = require ('mongoose');
const Schema = mongoose.Schema;

/**
 * Create Schema
 */
const contactSchema = new Schema({
    // _id: {
    //     type: Number,
    //     required: "_id is essential"
    // },
    name: {
        type: String,
        required: true
    },
    phoneNumber: {
        type: String,
        required: true
    },
    email: String,
    thumbnail: String,
    localCached: {
        type: Boolean,
        required: true
    }
});

module.exports = mongoose.model ('Contact', contactSchema)