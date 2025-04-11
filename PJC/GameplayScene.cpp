#include <SFML/Graphics.hpp>
#include <iostream>
#include <fmt/core.h>
#include <chrono>
#include <fstream>
#include <random>
#include "SFML/Audio.hpp"
#include "Scene.h"
#include "GameplayScene.h"
#include "LeaderBoardScene.h"


GameplayScene::GameplayScene(std::string params, Scene *pScene) : Scene(params) {
    font = sf::Font();
    level = params;
    auto file = "";
    if( level == "easy") {
        file = "../assets/dictionaries/words_basic.txt";
    } else if (level == "mid") {
        file = "../assets/dictionaries/words_medium.txt";
    } else {
        file = "../assets/dictionaries/words_advanced.txt";
    }
    std::ifstream wordsFile(file);
    if (!music.openFromFile("../assets/audio/Song8Bit.wav") || !font.loadFromFile("../assets/fonts/PixelifySans.ttf") || !wordsFile.is_open() || !heartTexture.loadFromFile("../assets/images/heart.png")) {
        fmt::print("Error loading assets! \n");
        return;
    }
    std::string line;
    while (std::getline(wordsFile, line)) {
        lines.push_back(line);
    }
    wordsFile.close();

    music.play();
    music.setLoop(true);
    startTime = std::chrono::system_clock::now();
    spawnerTime = std::chrono::system_clock::now();
    time = 0;

    for (int i = 0; i < health; i++) {
        sf::Sprite heart;
        heart.setTexture(heartTexture);
        heart.setPosition(32 + 36 * i, 704);
        hearts.push_back(heart);
    }

    timeText.setPosition(832, 704);
    wordsCountText.setPosition(448, 704);
    wpmText.setPosition(256, 704);
    velocityText.setPosition(640, 704);
    currentWordText.setPosition(64, 644);

    nextScene = pScene;
}

void GameplayScene::handleEvent(sf::RenderWindow &window, sf::Event event) {
    if (event.type == sf::Event::Closed) {
        window.close();
    }

    if (event.type == sf::Event::TextEntered) {
        if (event.text.unicode == 8) {
            if (currentWord.getSize() > 0) {
                currentWord.erase(currentWord.getSize() - 1, 1);
            }
        } else if (event.text.unicode == 13) {
            currentWord.clear();
        }  else {
            if (currentWord.getSize() < 50) {
                currentWord += event.text.unicode;
            }
        }

        for (size_t i = 0; i < words.size(); ++i) {
            if (words[i].getString() == currentWord) {
                words.erase(words.begin() + i);
                wordsCount += 1;
                currentWord.clear();
                break;
            }
        }

        wordsCountText.setString(sf::String(std::to_string(wordsCount)));
        currentWordText.setString(currentWord);
    }

}

void GameplayScene::update(sf::RenderWindow &window) {
    auto currentTime = std::chrono::system_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::seconds>(currentTime - startTime);
    time = duration.count();
    timeText.setString(sf::String(std::to_string(time) + "s"));
    auto timePassedFromLastSpawn = std::chrono::duration_cast<std::chrono::milliseconds>(
            currentTime - spawnerTime).count();

    if (time > 0) {
        wpm = (static_cast<double>(wordsCount) / time) * 60;
        std::ostringstream oss;
        oss.precision(2);
        oss << std::fixed << wpm;
        wpmText.setString(sf::String(oss.str() + "/min"));
    }

    if (timePassedFromLastSpawn > waitTime) {
        std::mt19937 gen(rd());
        std::uniform_int_distribution<> wordDistrib(0, lines.size() - 1);
        std::uniform_real_distribution<> spawnTimeDistrib(600, 1600);
        std::uniform_int_distribution<> spawnPosDistrib(64, 512);

        int randomIndex = wordDistrib(gen);
        waitTime = spawnTimeDistrib(gen);
        std::string randomElement = lines[randomIndex];
        int randomPos = spawnPosDistrib(gen);
        auto newWord = sf::Text(sf::String(randomElement), font, 24);
        newWord.setPosition(-128, randomPos);
        newWord.setFillColor(sf::Color::Green);
        words.push_back(newWord);
        spawnerTime = std::chrono::system_clock::now();
    }

    for (size_t i = 0; i < words.size(); ++i) {
        velocity = sf::Vector2f(startVelocity.x + static_cast<double>(time)/1000, startVelocity.y);
        std::ostringstream oss;
        oss.precision(3);
        oss << std::fixed << velocity.x;
        velocityText.setString(sf::String(oss.str() + "pix/ms"));
        words[i].setPosition(words[i].getPosition() + velocity);
        if (words[i].getPosition().x > 720) {
            words[i].setFillColor(sf::Color::Red);
        } else if (words[i].getPosition().x > 360) {
            words[i].setFillColor(sf::Color::Yellow);
        }
        if (words[i].getPosition().x > window.getSize().x) {
            words.erase(words.begin() + i);
            health -= 1;
            if(health <= 0) {
                delete nextScene;
                auto playerStats = std::to_string(wpm) + ";" + level + ";" + std::to_string(wordsCount) + ";" + std::to_string(time);
                nextScene = new LeaderBoardScene(playerStats, nextScene);
            }
            hearts.pop_back();
        }
    }

}

void GameplayScene::draw(sf::RenderWindow &window) {
    sf::RectangleShape rectangle(sf::Vector2f(1024, 128));
    rectangle.setFillColor(sf::Color::Black);
    rectangle.setPosition(0, 640);

    auto wordsCountLabelText = sf::Text("Words Count:", font, 24);
    wordsCountLabelText.setPosition(448, 672);

    auto wpmLabelText = sf::Text("WPM:", font, 24);
    wpmLabelText.setPosition(256, 672);

    auto timeLabelText = sf::Text("Time:", font, 24);
    timeLabelText.setPosition(832, 672);

    auto velocityLabelText = sf::Text("Velocity:", font, 24);
    velocityLabelText.setPosition(640, 672);

    auto healthLabelText = sf::Text("Health:", font, 24);
    healthLabelText.setPosition(32, 672);

    window.clear(sf::Color::Blue);
    window.draw(rectangle);
    window.draw(wordsCountLabelText);
    window.draw(wpmLabelText);
    window.draw(timeLabelText);
    window.draw(velocityLabelText);
    window.draw(healthLabelText);
    window.draw(timeText);
    window.draw(wpmText);
    window.draw(wordsCountText);
    for (auto heart: hearts) {
        window.draw(heart);
    }
    window.draw(velocityText);
    window.draw(currentWordText);
    for (size_t i = 0; i < words.size(); ++i) {
        window.draw(words[i]);
    }
    window.display();
}

