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
    // 选择网络链接钱包
    async addVTNetwork() {
        let me = this
        let provider = window.ethereum
        me.overlayshow = true
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
                    me.overlayshow = false
                    console.log("add network error");
                }
            }else{
                me.overlayshow = false
            }
        }
    },

    listNFTApprove() {
        let me = this
        let url = this.$api.nft.listNFTApprove
        let data = {
            id: this.NFTDetail.id,
            price: Number(this.price)
        }
        post(url, data, true)
            .then((res) => {
                if (res.code === 200) {
                    this.$router.replace(`/explore_details?id=${this.NFTDetail.id}`)
                }
                this.overlayshow = false
            })
            .catch((error) => {

            });
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
                this.Approved(provider)
            } else {
                this.listNFTApprove()
            }
            console.log("Approved address:", approvedAddress);
        } catch (error) {
            this.overlayshow = false
            this.$toast(error)
        }
    },
    async Approved(provider) {
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
            const approved = await contract.approve(this.NFTDetail.adminAddress, this.NFTDetail.id);
            this.listNFTApprove()
            console.log("Approved address:", approved);
        } catch (error) {
            this.overlayshow = false
            console.error(error);
        }
    },
    Complete() {
        if (!this.price) {
            this.$toast("Please enter the selling price")
        } else {
            this.addVTNetwork()
        }
    },
    getData() {
        this.overlayshow = true
        let url = this.$api.nft.getNFTDetail + `?id=${this.$route.query.id}`
        get(url)
            .then((res) => {
                if (res.code === 200) {
                    this.NFTDetail = res.data
                    this.overlayshow = false
                }
            })
            .catch((error) => { });
    },
}