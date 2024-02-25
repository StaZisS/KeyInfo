import {
    Button,
    Card,
    CardBody,
    CardHeader,
    CardTitle, Col, Collapse,
    Container, DropdownDivider,
    Form,
    FormCheck,
    FormControl,
    FormLabel, Row, ToggleButton
} from "react-bootstrap";
import {useState} from "react";
import KeysStore from "../../store/keysStore";

export const KeyFilter = () => {

    const [isOpen, setOpen] = useState(true)

    const [inAll, setInAll] = useState(KeysStore.keys_status === "" || KeysStore.keys_status === undefined)
    const [inDeanery, setInDeanery] = useState(KeysStore.keys_status === 'IN_DEANERY')
    const [inHand, setInHand] = useState(KeysStore.keys_status === 'IN_HAND')
    const [isHide, setIsHide] = useState(undefined)

    const handleSwitchInAll = (e) => {
        if (e.target.value === 'on') {
            setInAll(!inAll)
            KeysStore.setIsPrivate(undefined)
            if (inAll) {
                KeysStore.setIsPrivate(undefined)
                setInHand(false)
                setInDeanery(false)
                setIsHide(false)

            }
        }
    }


    const handleSwitchInHand = (e) => {
        if (e.target.value === 'on') {
            KeysStore.setIsPrivate(undefined)
            setInHand(true)
            setInDeanery(false)
            setIsHide(false)
        } else {
            setInHand(false)
            setInDeanery(true)
        }
    }

    const handleSwitchInDeanery = (e) => {
        if (e.target.value === 'on') {
            setInDeanery(true)
            setInHand(false)
        } else {
            setInDeanery(false)
            setInHand(true)
        }
    }


    return (
        <Container
            className='mt-3'>
            <Button className='w-100 d-md-none mb-3' aria-expanded={isOpen} onClick={() => {
                setOpen(!isOpen)
            }}>
                Фильтры
            </Button>
            <Collapse in={isOpen}>
                <div>
                    <Card className='rounded-0 rounded-top-3'>
                        <CardHeader>
                            <CardTitle>Фильтр ключей</CardTitle>
                        </CardHeader>
                        <CardBody>
                            <Form className='container-fluid'>
                                <Row className='d-flex gap-1'>
                                    <Col lg={inAll ? 4 : 2} sm={5} xs={12}>
                                        <FormControl min={0} value={KeysStore.build} max={30} onChange={(e) => {
                                            KeysStore.setBuild(e.target.value)
                                        }} placeholder={'Номер здания'} type='number'/>
                                    </Col>
                                    <Col lg={inAll ? 4 : 2} sm={5} xs={12}>
                                        <FormControl min={0} value={KeysStore.room} max={30} onChange={(e) => {
                                            KeysStore.setRoom(e.target.value)
                                        }} placeholder={'Номер аудитории'} type='number'/>
                                    </Col>
                                    <Col lg={inAll ? 3 : 2} md={6} sm={4} xs={12}>
                                        <FormCheck checked={inAll} onChange={(e) => {
                                            handleSwitchInAll(e)
                                            KeysStore.setKeyStatus(undefined)
                                        }} className='mt-2 mt-lg-0' type={'switch'} label={'Выбрать все'}/>
                                    </Col>
                                    {inAll === false && <>
                                        <Col lg={2} md={3} sm={4} xs={6}>
                                            <FormCheck checked={inDeanery} onChange={(e) => {
                                                KeysStore.setKeyStatus('IN_DEANERY')
                                                handleSwitchInDeanery(e)
                                            }} className='mt-2 mt-lg-0' type={'radio'} label={'В деканате'}/>
                                        </Col>
                                        <Col lg={2} md={3} sm={4} xs={6}>
                                            <FormCheck checked={inHand} onChange={(e) => {
                                                handleSwitchInHand(e)
                                                KeysStore.setKeyStatus('IN_HAND')
                                            }} className='mt-2 mt-lg-0' type={'radio'} label={'На руках'}/>
                                        </Col>
                                        <Col lg={1} md={3} sm={4} xs={6}>
                                            <FormCheck checked={isHide} onChange={(e) => {
                                                setIsHide(!isHide)
                                                KeysStore.setIsPrivate(!isHide)
                                            }} className='mt-2 mt-lg-0' type={'checkbox'} label={'Скрыт'}/>
                                        </Col>
                                    </>}
                                </Row>
                            </Form>
                        </CardBody>
                    </Card>
                </div>
            </Collapse>

        </Container>
    )
}