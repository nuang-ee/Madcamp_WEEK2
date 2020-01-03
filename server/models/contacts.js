const mongoose = require ('mongoose');
const schema = mongoose.Schema;

/**
 * Create Schema
 */
const contactsSchema = new schema({
    _id: String,
    phone_number: String,
    email: String,
    thumbnail: { data: Butter, contentType: String }
});

module.exports = mongoose.model('contacts', contactsSchema)