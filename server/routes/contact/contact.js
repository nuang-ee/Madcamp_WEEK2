const contactModel = require('../../models/contact');

exports.getContact = (req, res) => {
    const { userId } = req.params;

    try {
        const contact = contactModel.findById(userId, (err, results) => {
            if (err) {
                console.error(err)
                return;
            }
        });
        res.json(contact)
    } catch (e) {
        console.log(e);
        res.status(500).send(e);
    }
};

