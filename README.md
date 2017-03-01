# The Labyrinth

An online single player roguelike game with a REST API interface.

Before we start, a few caveats:

1. This is a work in progress; I will remove this caveat when the project is ready for prime time.

2. I don't plan to host this project. Instead, I'm offering it as open source so other people can host it.

3. There is no UI for this game; it's a video game without video. The purpose behind it is twofold; one,
I wanted to learn more about REST APIs and how to write a good automated test framework for them and two,
I think it would be interesting to see the sort of community spring up around a game that requires users
to build their own front end. The idea is that someone hosts the game, then that someone or some other
someone writes the front end code required to play the game. It's a REST API, so people can make mobile
apps, web apps, desktop apps, VR apps, or whatever apps. The level of the graphics can be anything from
totally immersive to text. But the most important idea is that there would be a community of developers
who shared their projects and continuously bounced ideas off each other, all with the intent of making
The Labyrinth a better game.

4. I'm a test engineer, not a developer. My primary goal is to build a test framework; the game is
secondary, really. So you may want to change some things before hosting the server.


# Getting Started
This API is supposed to be self-documenting. The first call you should make is to the instructions
endpoint; that will give you a list of endpoints you can call.

When you want to start working with an endpoint, the first thing you should do is call the OPTIONS
method; that will give you some basic information about the endpoint such as which methods are supported,
what the data should look like, and what related endpoints exist.