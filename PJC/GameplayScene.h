#ifndef MONKEYTYPER_GAMEPLAYSCENE_H
#define MONKEYTYPER_GAMEPLAYSCENE_H

#include "Scene.h"
#include <chrono>
#include <random>

class GameplayScene : public Scene {
public:
    GameplayScene(std::string params, Scene *pScene);

    void handleEvent(sf::RenderWindow &window, sf::Event event) override;

    void update(sf::RenderWindow &window) override;

    void draw(sf::RenderWindow &window) override;

    Scene *getNextScene() const override { return nextScene; }

private:
    Scene *nextScene;
    sf::Music music;
    std::string level;
    int health = 5;
    double wpm = 0;
    int time = 0;
    int wordsCount = 0;
    sf::Font font;
    std::chrono::time_point<std::chrono::system_clock> startTime;
    std::chrono::time_point<std::chrono::system_clock> spawnerTime;
    float waitTime = 0;
    sf::String currentWord;
    sf::Text timeText = sf::Text(sf::String(std::to_string(time) + "s"), font, 24);
    sf::Text wpmText = sf::Text(sf::String("0.00/min"), font, 24);;
    sf::Text wordsCountText = sf::Text(sf::String(std::to_string(wordsCount)), font, 24);;
    sf::Text velocityText = sf::Text(sf::String(std::to_string(velocity.x)), font, 24);;
    sf::Text currentWordText = sf::Text(currentWord, font, 24);;
    sf::Vector2f startVelocity = sf::Vector2f(0.005f, 0.00f);
    sf::Vector2f velocity = startVelocity;
    std::vector<sf::Text> words;
    std::vector<std::string> lines;
    std::vector<sf::Sprite> hearts;
    sf::Texture heartTexture;
    std::random_device rd;
};

#endif //MONKEYTYPER_GAMEPLAYSCENE_H
