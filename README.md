# Content Manager Service


Authored and developed by: Jhonatan Samuel Martinez Hernandez

This project is meant to be a Rest API with the main purpose of managing content that will be used to build websites. this is like a headless cms with minimal features, but with the advantage of being customizable, based in the clients needs. It will allow to manage content types, content and multimedia files.

It will be integrated to a front-end that I am developing using Angular framework and Material Design. which will provide to the user a grafical interface to interact with the API. A user friendly minimal dashboard.

you can check <a href="https://www.youtube.com/@JhonatanSamuelMartinezHernande">my youtube channel</a> where I show to the world how I am building the front-end and back-end for this project.

## Content types:

You can build types of content based on your needs. These types contain content fields of different kinds.
A content type represents the configuration for specific content. It will be used as a schema to
build 'content' of that same type. For instance, a "blog" content type is used just to build content for blogs.

The avilable supported fields are:

* Text
* RichText
* Media
* Date

More fields are planned to be added in the future and some of them are:

* Boolean
* Number
* JSON


## Content:

The "content" refers to the actual product resulting of building a new entry based in a content type previously created and saved in the database.

for instance, a blog content entry is the result of building a new entry using the "blog content type" as a reference.

## Media:

This feature allows to store in the server file system any of the supported multimedia content. By the moment only images and videos are planned to be used in the API. 

The users will be able to used these media files in any of the content types that has a "media" field in its configuration. a multimedia file can also be used in serveral content entries at the same time or just in one content entry.


## Future features to be included

* Login
* User Authentication
* User Authorization
* CACHE to increase speed on sql queries 