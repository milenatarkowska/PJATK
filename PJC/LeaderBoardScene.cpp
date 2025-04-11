#include "LeaderBoardScene.h"
#include <SFML/Graphics.hpp>
#include <fstream>
#include <sstream>
#include "Scene.h"
#include "MainMenuScene.h"


LeaderBoardScene::LeaderBoardScene(std::string params, Scene *pScene) : Scene(params) {
    std::fstream scoreFile("../score.txt", std::ios::out | std::ios::in);
    if (!scoreFile.is_open() || !font.loadFromFile("../assets/fonts/PixelifySans.ttf") ||
        !menuTexture.loadFromFile("../assets/images/MainMenuButton.png")) {
        std::ofstream newScoreFile("../score.txt");
        if (newScoreFile.is_open()) {
            newScoreFile.close();
        } else {
            fmt::print("Error loading assets! \n");
            return;
        }
    }

    std::string line;
    while (std::getline(scoreFile, line)) {
        std::stringstream ss(line);
        std::string text;

        while (std::getline(ss, text)) {
            fileScores.push_back(text);
        }
    }

    if (params != "") {
        fileScores.push_back(params);

        std::sort(fileScores.begin(), fileScores.end(), std::greater<std::string>());

        scoreFile.clear();
        scoreFile.seekg(0); //wraca na pozycję 0 w pliku

        for (size_t i = 0; i < fileScores.size(); ++i) {
            scoreFile << fileScores[i] + "\n";
        }
    }

    scoreFile.close();

    auto iter = 0;
    for (const auto &textLine: fileScores) {
        std::stringstream ss(textLine);
        std::string part;
        std::vector<std::string> splitLine;

        while (std::getline(ss, part, ';')) {
            splitLine.push_back(part);
        }

        menuButton.setTexture(menuTexture);
        sf::FloatRect menuBounds = menuButton.getLocalBounds();
        menuButton.setOrigin(menuBounds.left + menuBounds.width / 2, menuBounds.top);
        menuButton.setPosition(1024 / 2, 610);

        auto text = sf::String(splitLine[1] + "\t\t\t\t\t\t\t\t" + splitLine[0] + "\t\t\t\t\t\t\t\t" + splitLine[2] +
                               "\t\t\t\t\t\t\t\t" + splitLine[3]);
        auto newText = sf::Text(text, font, 24);
        sf::FloatRect textBounds = newText.getLocalBounds();
        newText.setOrigin(textBounds.left + textBounds.width / 2, textBounds.top);

        newText.setPosition(1024 / 2, 96 + 32 * iter);
        scores.push_back(newText);
        iter++;
        if (iter > 15) {
            break;
        }
    }

    nextScene = pScene;
}

void LeaderBoardScene::handleEvent(sf::RenderWindow &window, sf::Event event) {
    if (event.type == sf::Event::Closed) {
        window.close();
    } else if (event.type == sf::Event::MouseButtonPressed) {
        if (event.mouseButton.button == sf::Mouse::Left) {
            auto mousePos = sf::Mouse::getPosition(window);
            if (menuButton.getGlobalBounds().contains(
                    {static_cast<float>(mousePos.x), static_cast<float>(mousePos.y)})) {
                delete nextScene;
                nextScene = new MainMenuScene("", nextScene);
            }
        }
    }

}

void LeaderBoardScene::update(sf::RenderWindow &window) {

}

void LeaderBoardScene::draw(sf::RenderWindow &window) {
    auto text = "Level:" + std::string("\t\t\t\t\t\t\t") + "WPM:" + std::string("\t\t\t\t\t\t\t") + "Words Count:" +
                std::string("\t\t\t\t\t\t\t") + "Time [s]:";
    auto newText = sf::Text(text, font, 24);
    sf::FloatRect textBounds = newText.getLocalBounds();
    newText.setOrigin(textBounds.left + textBounds.width / 2, textBounds.top);
    newText.setPosition(1024 / 2, 32);


    window.clear();
    window.draw(newText);
    window.draw(menuButton);
    for (size_t i = 0; i < scores.size(); ++i) {
        window.draw(scores[i]);
    }
    window.display();
}

