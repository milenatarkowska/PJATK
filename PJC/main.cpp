#include <SFML/Graphics.hpp>
#include <fmt/core.h>
#include "Scene.h"
#include "MainMenuScene.h"


auto main() -> int{
    auto window = sf::RenderWindow(
            sf::VideoMode({1024, 768}), "MonkeyTyper",
    sf::Style::Titlebar | sf::Style::Close, sf::ContextSettings(0, 0, 0)
    );
    sf::Image icon;
    if (!icon.loadFromFile("../assets/images/icon.png")) {
        std::cerr << "Error: Unable to load icon.png" << "\n";
        return -1;
    }
    window.setIcon(icon.getSize().x, icon.getSize().y, icon.getPixelsPtr());

    Scene* currentScene = nullptr;
    currentScene = new MainMenuScene(sf::String(), currentScene);

    while (window.isOpen()) {
        sf::Event event;
        while (window.pollEvent(event)) {
            currentScene->handleEvent(window, event);
        }

        Scene* nextScene = dynamic_cast<Scene*>(currentScene);
        if (nextScene && nextScene->getNextScene() != nullptr) {
            auto tempScene = currentScene;
            currentScene = nextScene->getNextScene();
            delete tempScene;
        }

        currentScene->update(window);
        currentScene->draw(window);
    }

    return 0;
}
