#ifndef MONKEYTYPER_SCENE_H
#define MONKEYTYPER_SCENE_H

#include <SFML/Graphics.hpp>
#include <iostream>
#include <fmt/core.h>
#include "SFML/Audio.hpp"

class Scene {
public:
    Scene(std::string params) : nextScene(nullptr) {}

    virtual ~Scene() = default;

    virtual void handleEvent(sf::RenderWindow& window, sf::Event event) = 0;

    virtual void update(sf::RenderWindow& window) = 0;

    virtual void draw(sf::RenderWindow& window) = 0;

    virtual Scene* getNextScene() const { return nextScene; }

protected:
    Scene* nextScene;
};

#endif //MONKEYTYPER_SCENE_H
