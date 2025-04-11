#ifndef MAINMENUSCENE_H
#define MAINMENUSCENE_H

#include "Scene.h"
#include <SFML/Graphics.hpp>
#include <iostream>

class MainMenuScene : public Scene {
public:
    MainMenuScene(std::string params, Scene *pScene);

    void handleEvent(sf::RenderWindow& window, sf::Event event) override;
    void update(sf::RenderWindow& window) override;
    void draw(sf::RenderWindow& window) override;

    Scene* getNextScene() const override { return nextScene; }

private:
    Scene* nextScene;
    sf::Texture logoTexture;
    sf::Sprite logoSprite;
    sf::Texture startTexture;
    sf::Sprite buttonStart;
    sf::Texture leaderTexture;
    sf::Sprite buttonLeader;
    sf::Sprite easyButton;
    sf::Texture easyTexture;
    sf::Sprite midButton;
    sf::Texture midTexture;
    sf::Sprite hardButton;
    sf::Texture hardTexture;
    sf::String clickedButton;
    sf::Font font;
};

#endif // MAINMENUSCENE_H
