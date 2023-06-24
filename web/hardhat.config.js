/**
 * @type import('hardhat/config').HardhatUserConfig
 */
require("@nomiclabs/hardhat-waffle");

task("accounts", "Prints the list of accounts", async (taskArgs, hre) => {
  const accounts = await hre.ethers.getSigners();

  for (const account of accounts) {
    console.log(account.address);
  }
});

module.exports = {
  // networks: {
  //   rinkeby: {
  //     url: "https://rinkeby.infura.io/v3/7a26b6b8307444e4ba739f3122534c03",
  //     accounts: ["732b2bdb065aa3ac15193d33f9625ca49c019215d98981ce545c0223b18f4b23"],
  //   },
  // },
  solidity: "0.8.4",
};