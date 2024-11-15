# ChefMate

ChefMate is a mobile application designed to assist users in creating personalized recipe recommendations based on their available ingredients, cooking time, and number of diners. Built with a clean, Instagram-inspired interface, ChefMate enables users to explore recipes, save favorites, share with friends, and generate new recipes using AI.

## Features

1. **User Registration and Authentication**
   - Register with name, email, and password.
   - Log in securely with email and password.

2. **Recipe Recommendations**
   - Input groceries, number of diners, and available cooking time.
   - Receive personalized recipe suggestions using AI (powered by OpenAI).

3. **Favorites and Sharing**
   - Save favorite recipes to access them later.
   - Share saved recipes with friends for collaborative cooking.

4. **Meal Planning**
   - Create a custom meal by combining multiple recipes.

5. **Clean and User-Friendly Interface**
   - Professional and visually appealing design inspired by Instagram.
   - Avoids a noisy layout, focusing on ease of navigation and user experience.

6. **Speech Functionality**
   - Text-to-speech feature reads recipe instructions aloud.

## Technology Stack

- **Frontend**: Android (Java)
- **Backend**: Direct integration with OpenAI API (No custom backend)
- **Database**: Local storage for favorites and user settings
- **APIs**: OpenAI for recipe generation

## Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/username/chefmate.git

  Replace username with your GitHub username if necessary.

2. **Open the Project in Android Studio:**

- Launch Android Studio.
- Select Open and navigate to the cloned repository folder to open the ChefMate project.

3. **Configure OpenAI API Key:**

- ChefMate uses the OpenAI API to generate recipe suggestions. You need to configure an API key for this to work.
- Obtain an API key from OpenAI.
- Add your API key to the project by updating the configuration file (or add it to the environment as needed).

4. **Build and Run the Project:**

- Connect an Android device or set up an Android emulator in Android Studio.
- Click Run to build and deploy the application on your device or emulator.

## Usage

1. **Register or Log In**  
   - Open ChefMate and create an account by registering with your name, email, and password, or log in if you already have an account.

2. **Input Ingredients and Preferences**  
   - Enter the grocery items you have available, specify the number of diners, and set your available cooking time.

3. **Browse Recipe Recommendations**  
   - ChefMate will provide personalized recipe suggestions based on your inputs. Browse the list and select any recipe to view details.

4. **Save and Share Favorites**  
   - Mark recipes as favorites for easy access in the future. You can also share your favorite recipes with friends.

5. **Use Text-to-Speech for Hands-Free Cooking**  
   - ChefMate includes a text-to-speech feature that reads recipe instructions aloud, allowing for hands-free cooking.

## Screens and Layout

- **Home Screen**:  
  Contains input fields where users can enter groceries, select the number of diners, and set the available cooking time.

- **Recipe List Screen**:  
  Displays a list of recommended recipes based on the inputs from the Home Screen. Each recipe card provides a brief preview.

- **Recipe Detail Screen**:  
  Shows detailed instructions for a selected recipe, including ingredients, steps, and an option to save it as a favorite.

- **Favorites Screen**:  
  Displays a list of recipes that the user has marked as favorites, allowing quick access to saved recipes.

- **Shared Design Elements**:  
  - **Logo**: Displayed at the top of each screen, enhancing brand consistency.
  - **Menu Buttons**: Located at the bottom in black and gold, providing easy navigation.
  - **Background Color**: A soft beige (#F5F5DC) background, creating a clean and inviting user experience.

## Project Structure

- **MainActivity**:  
  Handles user input for ingredients, number of diners, and cooking time, and displays the recommended recipes generated by the AI.

- **RecipeDetailActivity**:  
  Displays detailed information for a selected recipe, including ingredients, instructions, and options to save or share the recipe.

- **FavoritesActivity**:  
  Manages the list of recipes that the user has marked as favorites for easy access and allows users to view recipe details from their saved list.

- **User Authentication**:  
  Handles user registration, login, and session management, enabling secure access to personalized content.

- **Text-to-Speech Integration**:  
  Provides audio playback of recipe instructions, enabling hands-free cooking by reading out steps to the user.

- **Networking**:  
  Manages API calls to the OpenAI service for recipe generation and handles responses from the server.

- **Data Storage**:  
  Local storage is used for managing user preferences, favorite recipes, and session data, ensuring data persistence across sessions.
```
