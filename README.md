# The Labyrinth

An online single player roguelike game with a REST API interface.

Before we start, a few caveats:

This is a work in progress; I'll remove this caveat when the project is ready for prime time.

I don't plan to host this project. Instead, I'm offering it as open source so other people can host it.

This video game is an API-first project. The main focus is on making the API and the test frameworks for
it; there's no real plan for a user interface. My hope is that people will be interested enough in
the game that they'll a) host the server for themselves and anyone else who wants to use it, and
b) build front end apps so non-programmers can play The Labyrinth. The most important goal for this
project is to create a community of developers sharing projects and ideas.

# Getting Started
##### Hosting the server
I've been running the server in Eclipse using Tomcat; this has limitations that may not scale very
well, so I encourage you to find your own server configuration.

I've used JDBC rather than a persistence layer. I'm not interested in learning how to use any
of the available persistence layers out there, so I haven't included them in The Labyrinth. I have,
however, tried to contain the JDBC calls to the models, so it should be relatively easy to replace them
with your favorite JDBC alternative.

##### Using the API
This API is supposed to be self-documenting. The first call you should make is to the instructions
endpoint; that will give you a list of endpoints you can call.

When you want to start working with an endpoint, the first thing you should do is call the OPTIONS
method; that will give you some basic information about the endpoint such as which methods are supported,
what the data should look like, and what related endpoints exist.

I've written an API client you can use to interface with the REST API, but feel free to write your own,
especially if you aren't writing code in Java.