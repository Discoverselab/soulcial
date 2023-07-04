/*General method definition
matters need attention
1. It cannot be defined by () = > {} arrow function.
2. A large number of calculation methods are suggested to be computed.

ps: https://cn.vuejs.org/v2/api/#methods
*/
import { set } from 'core-js/core/dict';
import {
    get, post,
} from '../../../http/http'
import {
    ethers
} from "ethers";
import Web3 from "web3";
import Sorl from "../../../libs/testEthABI.json";
const SoulAddress = "0xa947AF197bD5105d7f7C454139215fc37829cc86";
import { Toast } from 'vant';
export default {
    isUser(type) {
        if (type == 1) {
            return this.NFTDetail.mintUserAddress.toLocaleUpperCase() === this.$loginData.Auth_Token.toLocaleUpperCase()
        } else {
            return this.NFTDetail.ownerAddress.toLocaleUpperCase() === this.$loginData.Auth_Token.toLocaleUpperCase()
        }
    },
    linkUser(type) {
        if (this.isUser(type)) {
            this.$router.push(`/home`)
            return
        }
        let userId = type == 1 ? this.NFTDetail.mintUserId : this.NFTDetail.ownerUserId
        this.$router.push(`/user?id=${userId}`)
    },
    turnShowClick() {
        if (this.turnShow) {
            setTimeout(() => {
                this.turnShow = !this.turnShow
            }, 100)
        } else {
            this.turnShow = !this.turnShow
        }
    },
    urls() {
        var urls = ['Label_01', 'Label_02', 'Label_03', 'Label_04', 'Label_05', 'Label_06', 'Label_07', 'Label_08', 'Label_09', 'Label_10'];
        //Randomly get a value from the array
        return urls[Math.floor((Math.random() * urls.length))]
    },
    // 选择网络链接钱包
    async addVTNetwork() {
        let me = this
        let provider = window.ethereum
        try {
            await provider.request({
                method: "wallet_switchEthereumChain",
                params: [{ chainId: "0xAA36A7" }],
            });
            await provider.request({
                method: "eth_requestAccounts",
            });
            setTimeout(() => {
                me.getApproved(provider)
            }, 300)
        } catch (error) {
            if (error.code === 4902) {
                try {
                    await provider.request({
                        method: "wallet_addEthereumChain",
                        params: [
                            {
                                chainId: "0xAA36A7",
                                chainName: "Sepolia Test network",
                                nativeCurrency: {
                                    name: "Sepolia Test network",
                                    symbol: "SepoliaETH",
                                    decimals: 18,
                                },
                                rpcUrls: ['https://sepolia.infura.io/v3/'],
                                blockExplorerUrls: ['https://sepolia.etherscan.io'],
                                iconUrls: [""],
                            },
                        ],
                    });
                    await provider.request({
                        method: "eth_requestAccounts",
                    });
                    setTimeout(() => {
                        me.getApproved(provider)
                    }, 300)
                } catch (addError) {
                    console.log("add network error");
                }
            }
        }
    },

    async toPay() {
        let me = this
        let provider = window.ethereum
        provider.request({
            method: 'eth_sendTransaction',
            params: [{
                from: this.$loginData.Auth_Token,
                to: this.NFTDetail.adminAddress,
                value: ethers.utils.parseUnits(this.NFTDetail.price, 18)._hex,
            }]
        }).then((txHash) =>
            me.collectNFT(txHash)
        ).catch((error) =>
            console.error(error)
        );
    },
    async getApproved(provider) {
        let signer
        let providers
        providers = new ethers.providers.Web3Provider(provider)
        signer = providers.getSigner()
        const contract = new ethers.Contract(
            SoulAddress,
            Sorl,
            signer
        );
        try {
            const approvedAddress = await contract.getApproved(this.NFTDetail.id);
            if (approvedAddress.toLocaleUpperCase() != this.NFTDetail.adminAddress.toLocaleUpperCase()) {
                this.$toast("NFT anomaly")
            } else {
                this.toPay()
            }
            console.log("Approved address:", approvedAddress);
        } catch (error) {
            console.error(error);
        }

    },
    cancelListNFT() {
        this.overlayshow = true
        let url = this.$api.nft.cancelListNFT
        let data = {
            id: this.NFTDetail.id,
        }
        post(url, data, true)
            .then((res) => {
                if (res.code === 200) {
                    this.getData();
                }
                this.overlayshow = false
            })
            .catch((error) => {
                this.overlayshow = false
            });
    },
    set_click(type) {
        if (!this.$loginData.Auth_Token) {
            this.walletShow = true
        } else {
            if (type == 1) {
                this.getApproved()
                // this.collectNFT()
            }
        }
    },
    async collectNFT(txn) {
        console.log(txn)
        let me = this
        this.overlayshow = true
        let url = this.$api.nft.collectNFTOnline
        let data = {
            tokenId: this.NFTDetail.id,
            payAddress: this.$loginData.Auth_Token,
            txn: txn
        }
        post(url, data, true)
            .then((res) => {
                if (res.code === 200) {
                    setTimeout(() => {
                        me.overlayshow = false
                        me.$router.replace(`/purchase_success?id=${this.NFTDetail.id}`);
                    }, 2000)
                } else {
                    me.overlayshow = false
                    Toast(res.msg)
                }
            })
            .catch((error) => {
                me.overlayshow = false
                Toast(error.msg)
            });
    },
    getData() {
        this.overlayshow = true
        let url = this.$api.nft.getNFTDetail + `?id=${this.$route.query.id}`
        get(url)
            .then((res) => {
                if (res.code === 200) {
                    this.NFTDetail = res.data
                    let item = res.data
                    this.values.push(item.charisma)
                    this.values.push(item.courage)
                    this.values.push(item.art)
                    this.values.push(item.wisdom)
                    this.values.push(item.energy)
                    this.values.push(item.extroversion)
                    this.overlayshow = false
                }
            })
            .catch((error) => { });
    },
    substring(address) {
        if (!address) return
        return address.substring(0, 6) + '...' + address.substring(address.length - 5, address.length)
    },
}