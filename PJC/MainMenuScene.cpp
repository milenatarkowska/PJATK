#include <SFML/Graphics.hpp>
#include <iostream>
#include <fmt/core.h>
#include "SFML/Audio.hpp"
#include "Scene.h"
#include "MainMenuScene.h"
#include "GameplayScene.h"
#include "LeaderBoardScene.h"

MainMenuScene::MainMenuScene(std::string params, Scene *pScene) : Scene(params) {
    if (!logoTexture.loadFromFile("../assets/images/LOGO.png") || !startTexture.loadFromFile("../assets/images/StartButton.png") ||
        !leaderTexture.loadFromFile("../assets/images/LeaderButton.png") || !font.loadFromFile("../assets/fonts/PixelifySans.ttf") ||
        !easyTexture.loadFromFile("../assets/images/EASY.png") || !midTexture.loadFromFile("../assets/images/MID.png") ||
        !hardTexture.loadFromFile("../assets/images/HARD.png")) {
        fmt::print("Error loading assets! \n");
        return;
    }

    easyButton.setTexture(easyTexture);
    easyButton.setPosition({448, 480});

    midButton.setTexture(midTexture);
    midButton.setPosition({448, 544});

    hardButton.setTexture(hardTexture);
    hardButton.setPosition({448, 608});

    logoSprite.setTexture(logoTexture);
    logoSprite.setPosition({256, 256});

    buttonStart.setTexture(startTexture);
    buttonStart.setPosition({128, 512});

    buttonLeader.setTexture(leaderTexture);
    buttonLeader.setPosition({640, 512});

    midButton.setColor(sf::Color(50, 50, 50));
    hardButton.setColor(sf::Color(50, 50, 50));
    clickedButton = "easy";

    nextScene = pScene;
}

void MainMenuScene::handleEvent(sf::RenderWindow &window, sf::Event event) {
    if (event.type == sf::Event::Closed) {
        window.close();
    } else if (event.type == sf::Event::MouseButtonPressed) {
        if (event.mouseButton.button == sf::Mouse::Left) {
            auto mousePos = sf::Mouse::getPosition(window);
            if (buttonStart.getGlobalBounds().contains(
                    {static_cast<float>(mousePos.x), static_cast<float>(mousePos.y)})) {

                delete nextScene;
                nextScene = new GameplayScene(clickedButton, nextScene);
            }
            if (buttonLeader.getGlobalBounds().contains(
                    {static_cast<float>(mousePos.x), static_cast<float>(mousePos.y)})) {
                delete nextScene;
                nextScene = new LeaderBoardScene("", nextScene);
            }
            if (easyButton.getGlobalBounds().contains(
                    {static_cast<float>(mousePos.x), static_cast<float>(mousePos.y)})) {
                clickedButton = "easy";
                midButton.setColor(sf::Color(50, 50, 50));
                easyButton.setColor(sf::Color(255, 255, 255));
                hardButton.setColor(sf::Color(50, 50, 50));
            }
            if (midButton.getGlobalBounds().contains(
                    {static_cast<float>(mousePos.x), static_cast<float>(mousePos.y)})) {
                clickedButton = "mid";
                easyButton.setColor(sf::Color(50, 50, 50));
                midButton.setColor(sf::Color(255, 255, 255));
                hardButton.setColor(sf::Color(50, 50, 50));
            }
            if (hardButton.getGlobalBounds().contains(
                    {static_cast<float>(mousePos.x), static_cast<float>(mousePos.y)})) {
                clickedButton = "hard";
                midButton.setColor(sf::Color(50, 50, 50));
                hardButton.setColor(sf::Color(255, 255, 255));
                easyButton.setColor(sf::Color(50, 50, 50));
            }
        }
    }
}

void MainMenuScene::update(sf::RenderWindow &window) {
}

void MainMenuScene::draw(sf::RenderWindow &window) {
    window.clear();

    window.draw(easyButton);
    window.draw(midButton);
    window.draw(hardButton);
    window.draw(logoSprite);
    window.draw(buttonStart);
    window.draw(buttonLeader);
    window.display();
}
