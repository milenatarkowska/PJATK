#ifndef MONKEYTYPER_LEADERBOARDSCENE_H
#define MONKEYTYPER_LEADERBOARDSCENE_H

#include "Scene.h"

class LeaderBoardScene : public Scene {
public:
    LeaderBoardScene(std::string params, Scene *pScene);

    void handleEvent(sf::RenderWindow &window, sf::Event event) override;

    void update(sf::RenderWindow &window) override;

    void draw(sf::RenderWindow &window) override;

    Scene *getNextScene() const override { return nextScene; }

private:
    Scene *nextScene;
    sf::Font font;
    sf::Texture menuTexture;
    sf::Sprite menuButton;
    std::vector<sf::Text> scores;
    std::vector<std::string> fileScores;
};

#endif //MONKEYTYPER_LEADERBOARDSCENE_H
