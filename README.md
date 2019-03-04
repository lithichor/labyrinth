This is a video game based solely on a REST API. It started as a project where I could explore
some ideas I had about testing APIs.

Even though I describe what steps you'll need to take to get the server running (e.g., you
should use a better object-data mapper than the one I use) I don't expect anyone to take the
time to do so. This is really a way for me to hone my ideas and practice the techniques needed
for good automated tests.

If you are interested in testing techniques, visit my blog: lithichor.com. That's where I
discuss theoretical best testing practices.

# The Labyrinth

An online single player rogue-like game with a REST API interface.

Before we start, a few caveats:

This is a work in progress; I'll remove this caveat when the project is ready for prime time.

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
endpoint; that will give you a list of endpoints you can call. If you're working in linux, you can use
the curl command:

> curl http://localhost:8080/TheLabyrinth/api/instructions

When you want to start working with an endpoint, the first thing you should do is call the OPTIONS
method; that will give you some basic information about the endpoint such as which methods are supported,
what the data should look like, and what related endpoints exist.

Most endpoints require authentication when you call them. The Labyrinth uses Basic authentication; you need
to include the user's email and password in most of your requests:

> curl -u eric@eric.corn:password localhost:8080/TheLabyrinth/api/user -X GET

I've written an API client you can use to interface with the REST API, but feel free to write your own.
This is especially important if you aren't writing code in Java.
