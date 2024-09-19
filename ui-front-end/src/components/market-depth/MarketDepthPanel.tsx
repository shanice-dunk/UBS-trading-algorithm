import { MarketDepthRow } from "./useMarketDepthData";
import { PriceCell } from "./PriceCell";


interface MarketDepthPanelProps {
    data: MarketDepthRow[];
}

export const MarketDepthPanel = (props: MarketDepthPanelProps) => {
    console.log({props});
    return <table>
      <tr>
        <th></th>
        <th>Bid</th>
        <th>Ask</th>
        </tr>
        <tr>
        <th></th>
        <th>Quantity</th>
        <th>Price price={}</th>
        <th>Price</th>
        <th>Quantity</th>
        </tr>
        <tr>0</tr>
        <tr>1</tr>
        <tr>2</tr>
        <tr>3</tr>
        <tr>4</tr>
        <tr>5</tr>
        <tr>6</tr>
        <tr>7</tr>
        <tr>8</tr>
        <tr>9</tr>
    </table>
};